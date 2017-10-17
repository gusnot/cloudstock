import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BillItem } from './bill-item.model';
import { BillItemPopupService } from './bill-item-popup.service';
import { BillItemService } from './bill-item.service';

@Component({
    selector: 'jhi-bill-item-delete-dialog',
    templateUrl: './bill-item-delete-dialog.component.html'
})
export class BillItemDeleteDialogComponent {

    billItem: BillItem;

    constructor(
        private billItemService: BillItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.billItemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'billItemListModification',
                content: 'Deleted an billItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bill-item-delete-popup',
    template: ''
})
export class BillItemDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private billItemPopupService: BillItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.billItemPopupService
                .open(BillItemDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
