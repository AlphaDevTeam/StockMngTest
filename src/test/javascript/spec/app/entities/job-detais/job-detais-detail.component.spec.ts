/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StockMngTestModule } from '../../../test.module';
import { JobDetaisDetailComponent } from 'app/entities/job-detais/job-detais-detail.component';
import { JobDetais } from 'app/shared/model/job-detais.model';

describe('Component Tests', () => {
  describe('JobDetais Management Detail Component', () => {
    let comp: JobDetaisDetailComponent;
    let fixture: ComponentFixture<JobDetaisDetailComponent>;
    const route = ({ data: of({ jobDetais: new JobDetais(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StockMngTestModule],
        declarations: [JobDetaisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JobDetaisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobDetaisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobDetais).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
