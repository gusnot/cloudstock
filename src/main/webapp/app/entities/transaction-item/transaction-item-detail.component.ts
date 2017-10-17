import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TransactionItem } from './transaction-item.model';
import { TransactionItemService } from './transaction-item.service';

@Component({
    selector: 'jhi-transaction-item-detail',
    templateUrl: './transaction-item-detail.component.html'
})
export class TransactionItemDetailComponent implements OnInit, OnDestroy {

    transactionItem: TransactionItem;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private transactionItemService: TransactionItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTransactionItems();
    }

    load(id) {
        this.transactionItemService.find(id).subscribe((transactionItem) => {
            this.transactionItem = transactionItem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransactionItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'transactionItemListModification',
            (response) => this.load(this.transactionItem.id)
        );
    }
}
