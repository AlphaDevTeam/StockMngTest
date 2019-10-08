/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { StockMngTestModule } from '../../../test.module';
import { JobDetaisComponent } from 'app/entities/job-detais/job-detais.component';
import { JobDetaisService } from 'app/entities/job-detais/job-detais.service';
import { JobDetais } from 'app/shared/model/job-detais.model';

describe('Component Tests', () => {
  describe('JobDetais Management Component', () => {
    let comp: JobDetaisComponent;
    let fixture: ComponentFixture<JobDetaisComponent>;
    let service: JobDetaisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StockMngTestModule],
        declarations: [JobDetaisComponent],
        providers: []
      })
        .overrideTemplate(JobDetaisComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobDetaisComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobDetaisService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new JobDetais(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.jobDetais[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
