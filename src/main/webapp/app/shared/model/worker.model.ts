import { IJob } from 'app/shared/model/job.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IWorker {
  id?: number;
  workerCode?: string;
  workerName?: string;
  workerLimit?: number;
  isActive?: boolean;
  job?: IJob;
  location?: ILocation;
}

export class Worker implements IWorker {
  constructor(
    public id?: number,
    public workerCode?: string,
    public workerName?: string,
    public workerLimit?: number,
    public isActive?: boolean,
    public job?: IJob,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}
