import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  CashBookComponent,
  CashBookDetailComponent,
  CashBookUpdateComponent,
  CashBookDeletePopupComponent,
  CashBookDeleteDialogComponent,
  cashBookRoute,
  cashBookPopupRoute
} from './';

const ENTITY_STATES = [...cashBookRoute, ...cashBookPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CashBookComponent,
    CashBookDetailComponent,
    CashBookUpdateComponent,
    CashBookDeleteDialogComponent,
    CashBookDeletePopupComponent
  ],
  entryComponents: [CashBookComponent, CashBookUpdateComponent, CashBookDeleteDialogComponent, CashBookDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngCashBookModule {}
