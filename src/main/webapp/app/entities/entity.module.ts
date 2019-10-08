import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'products',
        loadChildren: () => import('./products/products.module').then(m => m.StockMngProductsModule)
      },
      {
        path: 'desings',
        loadChildren: () => import('./desings/desings.module').then(m => m.StockMngDesingsModule)
      },
      {
        path: 'job',
        loadChildren: () => import('./job/job.module').then(m => m.StockMngJobModule)
      },
      {
        path: 'job-detais',
        loadChildren: () => import('./job-detais/job-detais.module').then(m => m.StockMngJobDetaisModule)
      },
      {
        path: 'job-status',
        loadChildren: () => import('./job-status/job-status.module').then(m => m.StockMngJobStatusModule)
      },
      {
        path: 'items',
        loadChildren: () => import('./items/items.module').then(m => m.StockMngItemsModule)
      },
      {
        path: 'purchase-order',
        loadChildren: () => import('./purchase-order/purchase-order.module').then(m => m.StockMngPurchaseOrderModule)
      },
      {
        path: 'purchase-order-details',
        loadChildren: () => import('./purchase-order-details/purchase-order-details.module').then(m => m.StockMngPurchaseOrderDetailsModule)
      },
      {
        path: 'goods-receipt',
        loadChildren: () => import('./goods-receipt/goods-receipt.module').then(m => m.StockMngGoodsReceiptModule)
      },
      {
        path: 'goods-receipt-details',
        loadChildren: () => import('./goods-receipt-details/goods-receipt-details.module').then(m => m.StockMngGoodsReceiptDetailsModule)
      },
      {
        path: 'cash-book',
        loadChildren: () => import('./cash-book/cash-book.module').then(m => m.StockMngCashBookModule)
      },
      {
        path: 'document-type',
        loadChildren: () => import('./document-type/document-type.module').then(m => m.StockMngDocumentTypeModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.StockMngLocationModule)
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.StockMngCustomerModule)
      },
      {
        path: 'supplier',
        loadChildren: () => import('./supplier/supplier.module').then(m => m.StockMngSupplierModule)
      },
      {
        path: 'worker',
        loadChildren: () => import('./worker/worker.module').then(m => m.StockMngWorkerModule)
      },
      {
        path: 'ex-user',
        loadChildren: () => import('./ex-user/ex-user.module').then(m => m.StockMngExUserModule)
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.StockMngStockModule)
      },
      {
        path: 'company',
        loadChildren: () => import('./company/company.module').then(m => m.StockMngCompanyModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StockMngEntityModule {}
