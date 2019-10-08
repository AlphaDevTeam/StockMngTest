import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IExUser } from 'app/shared/model/ex-user.model';
import { AccountService } from 'app/core';
import { ExUserService } from './ex-user.service';

@Component({
  selector: 'jhi-ex-user',
  templateUrl: './ex-user.component.html'
})
export class ExUserComponent implements OnInit, OnDestroy {
  exUsers: IExUser[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected exUserService: ExUserService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.exUserService
      .query()
      .pipe(
        filter((res: HttpResponse<IExUser[]>) => res.ok),
        map((res: HttpResponse<IExUser[]>) => res.body)
      )
      .subscribe(
        (res: IExUser[]) => {
          this.exUsers = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInExUsers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExUser) {
    return item.id;
  }

  registerChangeInExUsers() {
    this.eventSubscriber = this.eventManager.subscribe('exUserListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
