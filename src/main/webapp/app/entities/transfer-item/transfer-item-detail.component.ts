import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TransferItem } from './transfer-item.model';
import { TransferItemService } from './transfer-item.service';

@Component({
    selector: 'jhi-transfer-item-detail',
    templateUrl: './transfer-item-detail.component.html'
})
export class TransferItemDetailComponent implements OnInit, OnDestroy {

    transferItem: TransferItem;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private transferItemService: TransferItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTransferItems();
    }

    load(id) {
        this.transferItemService.find(id).subscribe((transferItem) => {
            this.transferItem = transferItem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransferItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'transferItemListModification',
            (response) => this.load(this.transferItem.id)
        );
    }
}
