import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { AttributeItem } from './attribute-item.model';
import { AttributeItemService } from './attribute-item.service';

@Component({
    selector: 'jhi-attribute-item-detail',
    templateUrl: './attribute-item-detail.component.html'
})
export class AttributeItemDetailComponent implements OnInit, OnDestroy {

    attributeItem: AttributeItem;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private attributeItemService: AttributeItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAttributeItems();
    }

    load(id) {
        this.attributeItemService.find(id).subscribe((attributeItem) => {
            this.attributeItem = attributeItem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAttributeItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'attributeItemListModification',
            (response) => this.load(this.attributeItem.id)
        );
    }
}
