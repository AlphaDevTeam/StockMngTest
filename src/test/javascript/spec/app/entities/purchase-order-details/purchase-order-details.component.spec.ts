/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { StockMngTestModule } from '../../../test.module';
import { PurchaseOrderDetailsComponent } from 'app/entities/purchase-order-details/purchase-order-details.component';
import { PurchaseOrderDetailsService } from 'app/entities/purchase-order-details/purchase-order-details.service';
import { PurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';

describe('Component Tests', () => {
  describe('PurchaseOrderDetails Management Component', () => {
    let comp: PurchaseOrderDetailsComponent;
    let fixture: ComponentFixture<PurchaseOrderDetailsComponent>;
    let service: PurchaseOrderDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StockMngTestModule],
        declarations: [PurchaseOrderDetailsComponent],
        providers: []
      })
        .overrideTemplate(PurchaseOrderDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaseOrderDetailsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseOrderDetailsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PurchaseOrderDetails(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.purchaseOrderDetails[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
