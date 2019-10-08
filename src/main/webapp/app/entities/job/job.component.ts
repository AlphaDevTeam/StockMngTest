import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IJob } from 'app/shared/model/job.model';
import { AccountService } from 'app/core';
import { JobService } from './job.service';

@Component({
  selector: 'jhi-job',
  templateUrl: './job.component.html'
})
export class JobComponent implements OnInit, OnDestroy {
  jobs: IJob[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected jobService: JobService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.jobService
      .query()
      .pipe(
        filter((res: HttpResponse<IJob[]>) => res.ok),
        map((res: HttpResponse<IJob[]>) => res.body)
      )
      .subscribe(
        (res: IJob[]) => {
          this.jobs = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInJobs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IJob) {
    return item.id;
  }

  registerChangeInJobs() {
    this.eventSubscriber = this.eventManager.subscribe('jobListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
