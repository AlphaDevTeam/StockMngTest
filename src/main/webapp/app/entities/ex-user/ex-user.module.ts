import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  ExUserComponent,
  ExUserDetailComponent,
  ExUserUpdateComponent,
  ExUserDeletePopupComponent,
  ExUserDeleteDialogComponent,
  exUserRoute,
  exUserPopupRoute
} from './';

const ENTITY_STATES = [...exUserRoute, ...exUserPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ExUserComponent, ExUserDetailComponent, ExUserUpdateComponent, ExUserDeleteDialogComponent, ExUserDeletePopupComponent],
  entryComponents: [ExUserComponent, ExUserUpdateComponent, ExUserDeleteDialogComponent, ExUserDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngExUserModule {}
