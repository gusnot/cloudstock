import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Sku } from './sku.model';
import { SkuPopupService } from './sku-popup.service';
import { SkuService } from './sku.service';

@Component({
    selector: 'jhi-sku-delete-dialog',
    templateUrl: './sku-delete-dialog.component.html'
})
export class SkuDeleteDialogComponent {

    sku: Sku;

    constructor(
        private skuService: SkuService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.skuService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'skuListModification',
                content: 'Deleted an sku'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sku-delete-popup',
    template: ''
})
export class SkuDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private skuPopupService: SkuPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.skuPopupService
                .open(SkuDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
