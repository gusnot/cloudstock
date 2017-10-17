import { BaseEntity } from './../../shared';

export const enum TransactionStatus {
    'ASSIGNED',
    'APPROVED',
    'REJECTED'
}

export const enum TransactionType {
    'ADD',
    'DELETE',
    'ADJUST',
    'COUNT'
}

export class Transaction implements BaseEntity {
    constructor(
        public id?: number,
        public refNo?: string,
        public status?: TransactionStatus,
        public type?: TransactionType,
        public reason?: string,
        public transactionItems?: BaseEntity[],
        public branchId?: number,
    ) {
    }
}
