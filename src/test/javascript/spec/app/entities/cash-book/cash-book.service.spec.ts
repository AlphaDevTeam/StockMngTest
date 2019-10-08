/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CashBookService } from 'app/entities/cash-book/cash-book.service';
import { ICashBook, CashBook } from 'app/shared/model/cash-book.model';

describe('Service Tests', () => {
  describe('CashBook Service', () => {
    let injector: TestBed;
    let service: CashBookService;
    let httpMock: HttpTestingController;
    let elemDefault: ICashBook;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CashBookService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CashBook(0, currentDate, 'AAAAAAA', 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            cashbookDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a CashBook', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            cashbookDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            cashbookDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new CashBook(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a CashBook', async () => {
        const returnedFromService = Object.assign(
          {
            cashbookDate: currentDate.format(DATE_FORMAT),
            cashbookDescription: 'BBBBBB',
            cashbookAmountCR: 1,
            cashbookAmountDR: 1,
            cashbookBalance: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            cashbookDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of CashBook', async () => {
        const returnedFromService = Object.assign(
          {
            cashbookDate: currentDate.format(DATE_FORMAT),
            cashbookDescription: 'BBBBBB',
            cashbookAmountCR: 1,
            cashbookAmountDR: 1,
            cashbookBalance: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            cashbookDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CashBook', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
