import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TransferItem } from './transfer-item.model';
import { TransferItemPopupService } from './transfer-item-popup.service';
import { TransferItemService } from './transfer-item.service';

@Component({
    selector: 'jhi-transfer-item-delete-dialog',
    templateUrl: './transfer-item-delete-dialog.component.html'
})
export class TransferItemDeleteDialogComponent {

    transferItem: TransferItem;

    constructor(
        private transferItemService: TransferItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.transferItemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'transferItemListModification',
                content: 'Deleted an transferItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-transfer-item-delete-popup',
    template: ''
})
export class TransferItemDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transferItemPopupService: TransferItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.transferItemPopupService
                .open(TransferItemDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
