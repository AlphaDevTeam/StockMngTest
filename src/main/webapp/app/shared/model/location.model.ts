import { ICompany } from 'app/shared/model/company.model';
import { IUser } from 'app/core/user/user.model';

export interface ILocation {
  id?: number;
  locationCode?: string;
  locationName?: string;
  locationProfMargin?: number;
  isActive?: boolean;
  company?: ICompany;
  user?: IUser;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public locationCode?: string,
    public locationName?: string,
    public locationProfMargin?: number,
    public isActive?: boolean,
    public company?: ICompany,
    public user?: IUser
  ) {
    this.isActive = this.isActive || false;
  }
}
