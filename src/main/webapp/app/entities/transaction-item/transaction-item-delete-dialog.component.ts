import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TransactionItem } from './transaction-item.model';
import { TransactionItemPopupService } from './transaction-item-popup.service';
import { TransactionItemService } from './transaction-item.service';

@Component({
    selector: 'jhi-transaction-item-delete-dialog',
    templateUrl: './transaction-item-delete-dialog.component.html'
})
export class TransactionItemDeleteDialogComponent {

    transactionItem: TransactionItem;

    constructor(
        private transactionItemService: TransactionItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.transactionItemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'transactionItemListModification',
                content: 'Deleted an transactionItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-transaction-item-delete-popup',
    template: ''
})
export class TransactionItemDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transactionItemPopupService: TransactionItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.transactionItemPopupService
                .open(TransactionItemDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
