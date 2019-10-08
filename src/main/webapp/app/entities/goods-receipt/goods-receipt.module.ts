import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  GoodsReceiptComponent,
  GoodsReceiptDetailComponent,
  GoodsReceiptUpdateComponent,
  GoodsReceiptDeletePopupComponent,
  GoodsReceiptDeleteDialogComponent,
  GoodsReceiptDataTableComponent,
  goodsReceiptRoute,
  goodsReceiptPopupRoute
} from './';

const ENTITY_STATES = [...goodsReceiptRoute, ...goodsReceiptPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GoodsReceiptComponent,
    GoodsReceiptDetailComponent,
    GoodsReceiptUpdateComponent,
    GoodsReceiptDeleteDialogComponent,
    GoodsReceiptDataTableComponent,
    GoodsReceiptDeletePopupComponent
  ],
  entryComponents: [
    GoodsReceiptComponent,
    GoodsReceiptUpdateComponent,
    GoodsReceiptDeleteDialogComponent,
    GoodsReceiptDeletePopupComponent,
    GoodsReceiptDataTableComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngGoodsReceiptModule {}
