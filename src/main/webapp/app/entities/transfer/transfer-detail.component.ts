import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Transfer } from './transfer.model';
import { TransferService } from './transfer.service';

@Component({
    selector: 'jhi-transfer-detail',
    templateUrl: './transfer-detail.component.html'
})
export class TransferDetailComponent implements OnInit, OnDestroy {

    transfer: Transfer;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private transferService: TransferService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTransfers();
    }

    load(id) {
        this.transferService.find(id).subscribe((transfer) => {
            this.transfer = transfer;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransfers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'transferListModification',
            (response) => this.load(this.transfer.id)
        );
    }
}
