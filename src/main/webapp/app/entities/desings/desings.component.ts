import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDesings } from 'app/shared/model/desings.model';
import { AccountService } from 'app/core';
import { DesingsService } from './desings.service';

@Component({
  selector: 'jhi-desings',
  templateUrl: './desings.component.html'
})
export class DesingsComponent implements OnInit, OnDestroy {
  desings: IDesings[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected desingsService: DesingsService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.desingsService
      .query()
      .pipe(
        filter((res: HttpResponse<IDesings[]>) => res.ok),
        map((res: HttpResponse<IDesings[]>) => res.body)
      )
      .subscribe(
        (res: IDesings[]) => {
          this.desings = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDesings();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDesings) {
    return item.id;
  }

  registerChangeInDesings() {
    this.eventSubscriber = this.eventManager.subscribe('desingsListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
