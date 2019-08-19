import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { StockMngSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [StockMngSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [StockMngSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngSharedModule {
  static forRoot() {
    return {
      ngModule: StockMngSharedModule
    };
  }
}
