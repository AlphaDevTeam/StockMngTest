import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  StockComponent,
  StockDetailComponent,
  StockUpdateComponent,
  StockDeletePopupComponent,
  StockDeleteDialogComponent,
  stockRoute,
  stockPopupRoute
} from './';

const ENTITY_STATES = [...stockRoute, ...stockPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [StockComponent, StockDetailComponent, StockUpdateComponent, StockDeleteDialogComponent, StockDeletePopupComponent],
  entryComponents: [StockComponent, StockUpdateComponent, StockDeleteDialogComponent, StockDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngStockModule {}
