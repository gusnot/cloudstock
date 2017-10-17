import { BaseEntity } from './../../shared';

export const enum SellStatus {
    'OPENED',
    'CLOSED'
}

export class Sell implements BaseEntity {
    constructor(
        public id?: number,
        public refNo?: string,
        public status?: SellStatus,
        public bills?: BaseEntity[],
        public branchId?: number,
    ) {
    }
}
