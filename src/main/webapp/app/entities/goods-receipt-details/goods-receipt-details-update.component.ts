import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IGoodsReceiptDetails, GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { GoodsReceiptDetailsService } from './goods-receipt-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';
import { GoodsReceiptService } from 'app/entities/goods-receipt';

@Component({
  selector: 'jhi-goods-receipt-details-update',
  templateUrl: './goods-receipt-details-update.component.html'
})
export class GoodsReceiptDetailsUpdateComponent implements OnInit {
  isSaving: boolean;

  items: IItems[];

  goodsreceipts: IGoodsReceipt[];

  editForm = this.fb.group({
    id: [],
    grnQty: [null, [Validators.required]],
    item: [],
    grn: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected goodsReceiptDetailsService: GoodsReceiptDetailsService,
    protected itemsService: ItemsService,
    protected goodsReceiptService: GoodsReceiptService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ goodsReceiptDetails }) => {
      this.updateForm(goodsReceiptDetails);
    });
    this.itemsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IItems[]>) => mayBeOk.ok),
        map((response: HttpResponse<IItems[]>) => response.body)
      )
      .subscribe((res: IItems[]) => (this.items = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.goodsReceiptService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IGoodsReceipt[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGoodsReceipt[]>) => response.body)
      )
      .subscribe((res: IGoodsReceipt[]) => (this.goodsreceipts = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(goodsReceiptDetails: IGoodsReceiptDetails) {
    this.editForm.patchValue({
      id: goodsReceiptDetails.id,
      grnQty: goodsReceiptDetails.grnQty,
      item: goodsReceiptDetails.item,
      grn: goodsReceiptDetails.grn
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const goodsReceiptDetails = this.createFromForm();
    if (goodsReceiptDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.goodsReceiptDetailsService.update(goodsReceiptDetails));
    } else {
      this.subscribeToSaveResponse(this.goodsReceiptDetailsService.create(goodsReceiptDetails));
    }
  }

  private createFromForm(): IGoodsReceiptDetails {
    return {
      ...new GoodsReceiptDetails(),
      id: this.editForm.get(['id']).value,
      grnQty: this.editForm.get(['grnQty']).value,
      item: this.editForm.get(['item']).value,
      grn: this.editForm.get(['grn']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoodsReceiptDetails>>) {
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

  trackItemsById(index: number, item: IItems) {
    return item.id;
  }

  trackGoodsReceiptById(index: number, item: IGoodsReceipt) {
    return item.id;
  }
}
