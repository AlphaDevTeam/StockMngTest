import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { AccountService } from 'app/core';
import { GoodsReceiptService } from './goods-receipt.service';

@Component({
  selector: 'jhi-goods-receipt',
  templateUrl: './goods-receipt.component.html'
})
export class GoodsReceiptComponent implements OnInit, OnDestroy {
  goodsReceipts: IGoodsReceipt[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected goodsReceiptService: GoodsReceiptService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.goodsReceiptService
      .query()
      .pipe(
        filter((res: HttpResponse<IGoodsReceipt[]>) => res.ok),
        map((res: HttpResponse<IGoodsReceipt[]>) => res.body)
      )
      .subscribe(
        (res: IGoodsReceipt[]) => {
          this.goodsReceipts = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInGoodsReceipts();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGoodsReceipt) {
    return item.id;
  }

  registerChangeInGoodsReceipts() {
    this.eventSubscriber = this.eventManager.subscribe('goodsReceiptListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
