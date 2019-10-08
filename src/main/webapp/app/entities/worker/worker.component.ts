import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IWorker } from 'app/shared/model/worker.model';
import { AccountService } from 'app/core';
import { WorkerService } from './worker.service';

@Component({
  selector: 'jhi-worker',
  templateUrl: './worker.component.html'
})
export class WorkerComponent implements OnInit, OnDestroy {
  workers: IWorker[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected workerService: WorkerService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.workerService
      .query()
      .pipe(
        filter((res: HttpResponse<IWorker[]>) => res.ok),
        map((res: HttpResponse<IWorker[]>) => res.body)
      )
      .subscribe(
        (res: IWorker[]) => {
          this.workers = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInWorkers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IWorker) {
    return item.id;
  }

  registerChangeInWorkers() {
    this.eventSubscriber = this.eventManager.subscribe('workerListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
