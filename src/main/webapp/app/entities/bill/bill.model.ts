import { BaseEntity } from './../../shared';

export const enum BillStatus {
    'SELLING',
    'WAITING',
    'CANCELLED',
    'SOLD',
    'VOIDED'
}

export class Bill implements BaseEntity {
    constructor(
        public id?: number,
        public refNo?: string,
        public type?: BillStatus,
        public billItems?: BaseEntity[],
        public branchId?: number,
        public sellId?: number,
    ) {
    }
}
