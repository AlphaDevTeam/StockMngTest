import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IJobDetais } from 'app/shared/model/job-detais.model';

type EntityResponseType = HttpResponse<IJobDetais>;
type EntityArrayResponseType = HttpResponse<IJobDetais[]>;

@Injectable({ providedIn: 'root' })
export class JobDetaisService {
  public resourceUrl = SERVER_API_URL + 'api/job-detais';

  constructor(protected http: HttpClient) {}

  create(jobDetais: IJobDetais): Observable<EntityResponseType> {
    return this.http.post<IJobDetais>(this.resourceUrl, jobDetais, { observe: 'response' });
  }

  update(jobDetais: IJobDetais): Observable<EntityResponseType> {
    return this.http.put<IJobDetais>(this.resourceUrl, jobDetais, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobDetais>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobDetais[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
