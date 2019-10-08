import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  WorkerComponent,
  WorkerDetailComponent,
  WorkerUpdateComponent,
  WorkerDeletePopupComponent,
  WorkerDeleteDialogComponent,
  workerRoute,
  workerPopupRoute
} from './';

const ENTITY_STATES = [...workerRoute, ...workerPopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [WorkerComponent, WorkerDetailComponent, WorkerUpdateComponent, WorkerDeleteDialogComponent, WorkerDeletePopupComponent],
  entryComponents: [WorkerComponent, WorkerUpdateComponent, WorkerDeleteDialogComponent, WorkerDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngWorkerModule {}
