import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StockMngSharedModule } from 'app/shared';
import {
  DocumentTypeComponent,
  DocumentTypeDetailComponent,
  DocumentTypeUpdateComponent,
  DocumentTypeDeletePopupComponent,
  DocumentTypeDeleteDialogComponent,
  documentTypeRoute,
  documentTypePopupRoute
} from './';

const ENTITY_STATES = [...documentTypeRoute, ...documentTypePopupRoute];

@NgModule({
  imports: [StockMngSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DocumentTypeComponent,
    DocumentTypeDetailComponent,
    DocumentTypeUpdateComponent,
    DocumentTypeDeleteDialogComponent,
    DocumentTypeDeletePopupComponent
  ],
  entryComponents: [
    DocumentTypeComponent,
    DocumentTypeUpdateComponent,
    DocumentTypeDeleteDialogComponent,
    DocumentTypeDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngDocumentTypeModule {}
