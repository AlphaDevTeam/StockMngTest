import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  DesingsComponent,
  DesingsDetailComponent,
  DesingsUpdateComponent,
  DesingsDeletePopupComponent,
  DesingsDeleteDialogComponent,
  desingsRoute,
  desingsPopupRoute
} from './';

const ENTITY_STATES = [...desingsRoute, ...desingsPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DesingsComponent,
    DesingsDetailComponent,
    DesingsUpdateComponent,
    DesingsDeleteDialogComponent,
    DesingsDeletePopupComponent
  ],
  entryComponents: [DesingsComponent, DesingsUpdateComponent, DesingsDeleteDialogComponent, DesingsDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngDesingsModule {}
