import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { AccountService } from 'app/core';
import { GoodsReceiptDetailsService } from '../goods-receipt-details/goods-receipt-details.service';

@Component({
  selector: 'jhi-details-data-table',
  templateUrl: 'goods-receipt-data-table.component.html'
})
export class GoodsReceiptDataTableComponent implements OnInit, OnDestroy {
  goodsReceiptDetails: IGoodsReceiptDetails[];
  tempGoodsReceiptDetails: IGoodsReceiptDetails[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.goodsReceiptDetailsService
      .query()
      .pipe(
        filter((res: HttpResponse<IGoodsReceiptDetails[]>) => res.ok),
        map((res: HttpResponse<IGoodsReceiptDetails[]>) => res.body)
      )
      .subscribe(
        (res: IGoodsReceiptDetails[]) => {
          this.goodsReceiptDetails = res;
          console.log(res);
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    //this.loadAll();

    this.tempGoodsReceiptDetails = [{ id: 1, grnQty: 'solution connecting', grn: null, item: null }];
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGoodsReceiptDetails) {
    return item.id;
  }

  registerChangeInGoodsReceiptDetails() {
    this.eventSubscriber = this.eventManager.subscribe('goodsReceiptDetailsListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
