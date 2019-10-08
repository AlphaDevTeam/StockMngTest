import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ILocation, Location } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html'
})
export class LocationUpdateComponent implements OnInit {
  isSaving: boolean;

  companies: ICompany[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    locationCode: [null, [Validators.required]],
    locationName: [null, [Validators.required]],
    locationProfMargin: [null, [Validators.required]],
    isActive: [],
    company: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected locationService: LocationService,
    protected companyService: CompanyService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ location }) => {
      this.updateForm(location);
    });
    this.companyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe((res: ICompany[]) => (this.companies = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(location: ILocation) {
    this.editForm.patchValue({
      id: location.id,
      locationCode: location.locationCode,
      locationName: location.locationName,
      locationProfMargin: location.locationProfMargin,
      isActive: location.isActive,
      company: location.company,
      user: location.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const location = this.createFromForm();
    if (location.id !== undefined) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  private createFromForm(): ILocation {
    return {
      ...new Location(),
      id: this.editForm.get(['id']).value,
      locationCode: this.editForm.get(['locationCode']).value,
      locationName: this.editForm.get(['locationName']).value,
      locationProfMargin: this.editForm.get(['locationProfMargin']).value,
      isActive: this.editForm.get(['isActive']).value,
      company: this.editForm.get(['company']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>) {
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

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
