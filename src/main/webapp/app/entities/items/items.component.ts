import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IItems } from 'app/shared/model/items.model';
import { AccountService } from 'app/core';
import { ItemsService } from './items.service';

@Component({
  selector: 'jhi-items',
  templateUrl: './items.component.html'
})
export class ItemsComponent implements OnInit, OnDestroy {
  items: IItems[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected itemsService: ItemsService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.itemsService
      .query()
      .pipe(
        filter((res: HttpResponse<IItems[]>) => res.ok),
        map((res: HttpResponse<IItems[]>) => res.body)
      )
      .subscribe(
        (res: IItems[]) => {
          this.items = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInItems();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IItems) {
    return item.id;
  }

  registerChangeInItems() {
    this.eventSubscriber = this.eventManager.subscribe('itemsListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
