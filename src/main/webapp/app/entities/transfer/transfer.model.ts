import { BaseEntity } from './../../shared';

export const enum TransactionStatus {
    'ASSIGNED',
    'APPROVED',
    'REJECTED'
}

export class Transfer implements BaseEntity {
    constructor(
        public id?: number,
        public refNo?: string,
        public status?: TransactionStatus,
        public transferItems?: BaseEntity[],
        public srcBranchId?: number,
        public destBranchId?: number,
    ) {
    }
}
