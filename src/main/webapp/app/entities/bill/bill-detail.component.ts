import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Bill } from './bill.model';
import { BillService } from './bill.service';

@Component({
    selector: 'jhi-bill-detail',
    templateUrl: './bill-detail.component.html'
})
export class BillDetailComponent implements OnInit, OnDestroy {

    bill: Bill;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private billService: BillService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBills();
    }

    load(id) {
        this.billService.find(id).subscribe((bill) => {
            this.bill = bill;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBills() {
        this.eventSubscriber = this.eventManager.subscribe(
            'billListModification',
            (response) => this.load(this.bill.id)
        );
    }
}
