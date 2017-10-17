import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AttributeItem } from './attribute-item.model';
import { AttributeItemPopupService } from './attribute-item-popup.service';
import { AttributeItemService } from './attribute-item.service';

@Component({
    selector: 'jhi-attribute-item-delete-dialog',
    templateUrl: './attribute-item-delete-dialog.component.html'
})
export class AttributeItemDeleteDialogComponent {

    attributeItem: AttributeItem;

    constructor(
        private attributeItemService: AttributeItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.attributeItemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'attributeItemListModification',
                content: 'Deleted an attributeItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-attribute-item-delete-popup',
    template: ''
})
export class AttributeItemDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attributeItemPopupService: AttributeItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.attributeItemPopupService
                .open(AttributeItemDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
