import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IWorker, Worker } from 'app/shared/model/worker.model';
import { WorkerService } from './worker.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';

@Component({
  selector: 'jhi-worker-update',
  templateUrl: './worker-update.component.html'
})
export class WorkerUpdateComponent implements OnInit {
  isSaving: boolean;

  jobs: IJob[];

  locations: ILocation[];

  editForm = this.fb.group({
    id: [],
    workerCode: [null, [Validators.required]],
    workerName: [null, [Validators.required]],
    workerLimit: [],
    isActive: [],
    job: [],
    location: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected workerService: WorkerService,
    protected jobService: JobService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ worker }) => {
      this.updateForm(worker);
    });
    this.jobService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IJob[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJob[]>) => response.body)
      )
      .subscribe((res: IJob[]) => (this.jobs = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
        map((response: HttpResponse<ILocation[]>) => response.body)
      )
      .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(worker: IWorker) {
    this.editForm.patchValue({
      id: worker.id,
      workerCode: worker.workerCode,
      workerName: worker.workerName,
      workerLimit: worker.workerLimit,
      isActive: worker.isActive,
      job: worker.job,
      location: worker.location
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const worker = this.createFromForm();
    if (worker.id !== undefined) {
      this.subscribeToSaveResponse(this.workerService.update(worker));
    } else {
      this.subscribeToSaveResponse(this.workerService.create(worker));
    }
  }

  private createFromForm(): IWorker {
    return {
      ...new Worker(),
      id: this.editForm.get(['id']).value,
      workerCode: this.editForm.get(['workerCode']).value,
      workerName: this.editForm.get(['workerName']).value,
      workerLimit: this.editForm.get(['workerLimit']).value,
      isActive: this.editForm.get(['isActive']).value,
      job: this.editForm.get(['job']).value,
      location: this.editForm.get(['location']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorker>>) {
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

  trackJobById(index: number, item: IJob) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }
}
