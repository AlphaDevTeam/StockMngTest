import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICashBook } from 'app/shared/model/cash-book.model';
import { AccountService } from 'app/core';
import { CashBookService } from './cash-book.service';

@Component({
  selector: 'jhi-cash-book',
  templateUrl: './cash-book.component.html'
})
export class CashBookComponent implements OnInit, OnDestroy {
  cashBooks: ICashBook[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected cashBookService: CashBookService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.cashBookService
      .query()
      .pipe(
        filter((res: HttpResponse<ICashBook[]>) => res.ok),
        map((res: HttpResponse<ICashBook[]>) => res.body)
      )
      .subscribe(
        (res: ICashBook[]) => {
          this.cashBooks = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCashBooks();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICashBook) {
    return item.id;
  }

  registerChangeInCashBooks() {
    this.eventSubscriber = this.eventManager.subscribe('cashBookListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
