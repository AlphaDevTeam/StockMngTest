import { Moment } from 'moment';
import { ILocation } from 'app/shared/model/location.model';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { IItems } from 'app/shared/model/items.model';

export interface ICashBook {
  id?: number;
  cashbookDate?: Moment;
  cashbookDescription?: string;
  cashbookAmountCR?: number;
  cashbookAmountDR?: number;
  cashbookBalance?: number;
  location?: ILocation;
  documentType?: IDocumentType;
  item?: IItems;
}

export class CashBook implements ICashBook {
  constructor(
    public id?: number,
    public cashbookDate?: Moment,
    public cashbookDescription?: string,
    public cashbookAmountCR?: number,
    public cashbookAmountDR?: number,
    public cashbookBalance?: number,
    public location?: ILocation,
    public documentType?: IDocumentType,
    public item?: IItems
  ) {}
}
