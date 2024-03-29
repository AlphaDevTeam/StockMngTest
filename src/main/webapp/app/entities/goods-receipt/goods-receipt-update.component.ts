import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IGoodsReceipt, GoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from './goods-receipt.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';

@Component({
  selector: 'jhi-goods-receipt-update',
  templateUrl: './goods-receipt-update.component.html'
})
export class GoodsReceiptUpdateComponent implements OnInit {
  isSaving: boolean;

  suppliers: ISupplier[];

  locations: ILocation[];
  grnDateDp: any;

  editForm = this.fb.group({
    id: [],
    grnNumber: [null, [Validators.required]],
    grnDate: [null, [Validators.required]],
    poNumber: [],
    supplier: [],
    location: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected goodsReceiptService: GoodsReceiptService,
    protected supplierService: SupplierService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ goodsReceipt }) => {
      this.updateForm(goodsReceipt);
    });
    this.supplierService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISupplier[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISupplier[]>) => response.body)
      )
      .subscribe((res: ISupplier[]) => (this.suppliers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(goodsReceipt: IGoodsReceipt) {
    this.editForm.patchValue({
      id: goodsReceipt.id,
      grnNumber: goodsReceipt.grnNumber,
      grnDate: goodsReceipt.grnDate,
      poNumber: goodsReceipt.poNumber,
      supplier: goodsReceipt.supplier,
      location: goodsReceipt.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const goodsReceipt = this.createFromForm();
    if (goodsReceipt.id !== undefined) {
      this.subscribeToSaveResponse(this.goodsReceiptService.update(goodsReceipt));
    } else {
      this.subscribeToSaveResponse(this.goodsReceiptService.create(goodsReceipt));
    }
  }

  private createFromForm(): IGoodsReceipt {
    return {
      ...new GoodsReceipt(),
      id: this.editForm.get(['id']).value,
      grnNumber: this.editForm.get(['grnNumber']).value,
      grnDate: this.editForm.get(['grnDate']).value,
      poNumber: this.editForm.get(['poNumber']).value,
      supplier: this.editForm.get(['supplier']).value,
      location: this.editForm.get(['location']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoodsReceipt>>) {
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

  trackSupplierById(index: number, item: ISupplier) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }
}
