import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { ICashBook, CashBook } from 'app/shared/model/cash-book.model';
import { CashBookService } from './cash-book.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items';

@Component({
  selector: 'jhi-cash-book-update',
  templateUrl: './cash-book-update.component.html'
})
export class CashBookUpdateComponent implements OnInit {
  isSaving: boolean;

  locations: ILocation[];

  documenttypes: IDocumentType[];

  items: IItems[];
  cashbookDateDp: any;

  editForm = this.fb.group({
    id: [],
    cashbookDate: [null, [Validators.required]],
    cashbookDescription: [null, [Validators.required]],
    cashbookAmountCR: [null, [Validators.required]],
    cashbookAmountDR: [null, [Validators.required]],
    cashbookBalance: [null, [Validators.required]],
    location: [],
    documentType: [],
    item: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cashBookService: CashBookService,
    protected locationService: LocationService,
    protected documentTypeService: DocumentTypeService,
    protected itemsService: ItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ cashBook }) => {
      this.updateForm(cashBook);
    });
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.documentTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDocumentType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDocumentType[]>) => response.body)
      )
      .subscribe((res: IDocumentType[]) => (this.documenttypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.itemsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IItems[]>) => mayBeOk.ok),
        map((response: HttpResponse<IItems[]>) => response.body)
      )
      .subscribe((res: IItems[]) => (this.items = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(cashBook: ICashBook) {
    this.editForm.patchValue({
      id: cashBook.id,
      cashbookDate: cashBook.cashbookDate,
      cashbookDescription: cashBook.cashbookDescription,
      cashbookAmountCR: cashBook.cashbookAmountCR,
      cashbookAmountDR: cashBook.cashbookAmountDR,
      cashbookBalance: cashBook.cashbookBalance,
      location: cashBook.location,
      documentType: cashBook.documentType,
      item: cashBook.item
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const cashBook = this.createFromForm();
    if (cashBook.id !== undefined) {
      this.subscribeToSaveResponse(this.cashBookService.update(cashBook));
    } else {
      this.subscribeToSaveResponse(this.cashBookService.create(cashBook));
    }
  }

  private createFromForm(): ICashBook {
    return {
      ...new CashBook(),
      id: this.editForm.get(['id']).value,
      cashbookDate: this.editForm.get(['cashbookDate']).value,
      cashbookDescription: this.editForm.get(['cashbookDescription']).value,
      cashbookAmountCR: this.editForm.get(['cashbookAmountCR']).value,
      cashbookAmountDR: this.editForm.get(['cashbookAmountDR']).value,
      cashbookBalance: this.editForm.get(['cashbookBalance']).value,
      location: this.editForm.get(['location']).value,
      documentType: this.editForm.get(['documentType']).value,
      item: this.editForm.get(['item']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashBook>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackDocumentTypeById(index: number, item: IDocumentType) {
    return item.id;
  }

  trackItemsById(index: number, item: IItems) {
    return item.id;
  }
}
