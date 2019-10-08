import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  GoodsReceiptDetailsComponent,
  GoodsReceiptDetailsDetailComponent,
  GoodsReceiptDetailsUpdateComponent,
  GoodsReceiptDetailsDeletePopupComponent,
  GoodsReceiptDetailsDeleteDialogComponent,
  goodsReceiptDetailsRoute,
  goodsReceiptDetailsPopupRoute
} from './';

const ENTITY_STATES = [...goodsReceiptDetailsRoute, ...goodsReceiptDetailsPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GoodsReceiptDetailsComponent,
    GoodsReceiptDetailsDetailComponent,
    GoodsReceiptDetailsUpdateComponent,
    GoodsReceiptDetailsDeleteDialogComponent,
    GoodsReceiptDetailsDeletePopupComponent
  ],
  entryComponents: [
    GoodsReceiptDetailsComponent,
    GoodsReceiptDetailsUpdateComponent,
    GoodsReceiptDetailsDeleteDialogComponent,
    GoodsReceiptDetailsDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngGoodsReceiptDetailsModule {}
