import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Sku } from './sku.model';
import { SkuService } from './sku.service';

@Component({
    selector: 'jhi-sku-detail',
    templateUrl: './sku-detail.component.html'
})
export class SkuDetailComponent implements OnInit, OnDestroy {

    sku: Sku;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private skuService: SkuService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSkus();
    }

    load(id) {
        this.skuService.find(id).subscribe((sku) => {
            this.sku = sku;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSkus() {
        this.eventSubscriber = this.eventManager.subscribe(
            'skuListModification',
            (response) => this.load(this.sku.id)
        );
    }
}
