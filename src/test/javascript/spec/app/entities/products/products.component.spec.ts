/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { StockMngTestModule } from '../../../test.module';
import { ProductsComponent } from 'app/entities/products/products.component';
import { ProductsService } from 'app/entities/products/products.service';
import { Products } from 'app/shared/model/products.model';

describe('Component Tests', () => {
  describe('Products Management Component', () => {
    let comp: ProductsComponent;
    let fixture: ComponentFixture<ProductsComponent>;
    let service: ProductsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StockMngTestModule],
        declarations: [ProductsComponent],
        providers: []
      })
        .overrideTemplate(ProductsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Products(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.products[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
