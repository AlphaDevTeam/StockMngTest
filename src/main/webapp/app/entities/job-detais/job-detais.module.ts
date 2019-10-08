import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  JobDetaisComponent,
  JobDetaisDetailComponent,
  JobDetaisUpdateComponent,
  JobDetaisDeletePopupComponent,
  JobDetaisDeleteDialogComponent,
  jobDetaisRoute,
  jobDetaisPopupRoute
} from './';

const ENTITY_STATES = [...jobDetaisRoute, ...jobDetaisPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    JobDetaisComponent,
    JobDetaisDetailComponent,
    JobDetaisUpdateComponent,
    JobDetaisDeleteDialogComponent,
    JobDetaisDeletePopupComponent
  ],
  entryComponents: [JobDetaisComponent, JobDetaisUpdateComponent, JobDetaisDeleteDialogComponent, JobDetaisDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngJobDetaisModule {}
