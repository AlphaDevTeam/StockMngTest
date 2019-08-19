import { NgModule } from '@angular/core';

import { StockMngSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [StockMngSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [StockMngSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class StockMngSharedCommonModule {}
