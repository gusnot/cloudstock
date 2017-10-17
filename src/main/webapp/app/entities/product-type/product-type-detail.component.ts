import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ProductType } from './product-type.model';
import { ProductTypeService } from './product-type.service';

@Component({
    selector: 'jhi-product-type-detail',
    templateUrl: './product-type-detail.component.html'
})
export class ProductTypeDetailComponent implements OnInit, OnDestroy {

    productType: ProductType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private productTypeService: ProductTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProductTypes();
    }

    load(id) {
        this.productTypeService.find(id).subscribe((productType) => {
            this.productType = productType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProductTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'productTypeListModification',
            (response) => this.load(this.productType.id)
        );
    }
}
