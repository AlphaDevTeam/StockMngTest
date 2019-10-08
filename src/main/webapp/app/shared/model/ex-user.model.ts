import { IUser } from 'app/core/user/user.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IExUser {
  id?: number;
  userKey?: string;
  relatedUser?: IUser;
  company?: ICompany;
}

export class ExUser implements IExUser {
  constructor(public id?: number, public userKey?: string, public relatedUser?: IUser, public company?: ICompany) {}
}
