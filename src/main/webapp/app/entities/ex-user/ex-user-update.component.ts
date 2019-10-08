import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IExUser, ExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';
import { IUser, UserService } from 'app/core';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
  selector: 'jhi-ex-user-update',
  templateUrl: './ex-user-update.component.html'
})
export class ExUserUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  companies: ICompany[];

  editForm = this.fb.group({
    id: [],
    userKey: [],
    relatedUser: [],
    company: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected exUserService: ExUserService,
    protected userService: UserService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.updateForm(exUser);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.companyService
      .query({ 'exUserId.specified': 'false' })
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe(
        (res: ICompany[]) => {
          if (!this.editForm.get('company').value || !this.editForm.get('company').value.id) {
            this.companies = res;
          } else {
            this.companyService
              .find(this.editForm.get('company').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<ICompany>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<ICompany>) => subResponse.body)
              )
              .subscribe(
                (subRes: ICompany) => (this.companies = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(exUser: IExUser) {
    this.editForm.patchValue({
      id: exUser.id,
      userKey: exUser.userKey,
      relatedUser: exUser.relatedUser,
      company: exUser.company
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const exUser = this.createFromForm();
    if (exUser.id !== undefined) {
      this.subscribeToSaveResponse(this.exUserService.update(exUser));
    } else {
      this.subscribeToSaveResponse(this.exUserService.create(exUser));
    }
  }

  private createFromForm(): IExUser {
    return {
      ...new ExUser(),
      id: this.editForm.get(['id']).value,
      userKey: this.editForm.get(['userKey']).value,
      relatedUser: this.editForm.get(['relatedUser']).value,
      company: this.editForm.get(['company']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExUser>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }
}
