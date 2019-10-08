import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  PurchaseOrderDetailsComponent,
  PurchaseOrderDetailsDetailComponent,
  PurchaseOrderDetailsUpdateComponent,
  PurchaseOrderDetailsDeletePopupComponent,
  PurchaseOrderDetailsDeleteDialogComponent,
  purchaseOrderDetailsRoute,
  purchaseOrderDetailsPopupRoute
} from './';

const ENTITY_STATES = [...purchaseOrderDetailsRoute, ...purchaseOrderDetailsPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PurchaseOrderDetailsComponent,
    PurchaseOrderDetailsDetailComponent,
    PurchaseOrderDetailsUpdateComponent,
    PurchaseOrderDetailsDeleteDialogComponent,
    PurchaseOrderDetailsDeletePopupComponent
  ],
  entryComponents: [
    PurchaseOrderDetailsComponent,
    PurchaseOrderDetailsUpdateComponent,
    PurchaseOrderDetailsDeleteDialogComponent,
    PurchaseOrderDetailsDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngPurchaseOrderDetailsModule {}
