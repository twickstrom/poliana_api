package com.poliana.bills.repositories;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.poliana.bills.entities.VoteGT.VoteGtMorphia;
import com.poliana.bills.entities.Bill;
import com.poliana.bills.entities.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

/**
 * @author David Gilmore
 * @date 11/17/13
 */
@Repository
public class BillMorphiaRepo {

    @Autowired
    private Datastore mongoStore;

    public Vote voteByVoteId(String voteId) {
        Query<Vote> voteQuery =
                mongoStore.find(Vote.class,"voteId",voteId);
        return voteQuery.get();
    }

    public Iterator<VoteGtMorphia> govtrackVotesByCongress(int congress) {
        Query<VoteGtMorphia> query =
                mongoStore.find(VoteGtMorphia.class, "congress", congress);
        return query.iterator();
    }

    public Bill billByBillId(String billId) {
        Query<Bill> billQuery =
                mongoStore.find(Bill.class,"billId",billId);
        return billQuery.get();
    }

    public Key<Vote> saveVote(Vote vote) {
        return mongoStore.save(vote);
    }

    public Iterable<Key<Bill>> saveBills(List<Bill> bills) {
        return mongoStore.save(bills);
    }

    public Iterable<Key<Vote>> saveVotes(List<Vote> votes) {
        return mongoStore.save(votes);
    }

    public UpdateResults<Bill> updateBill(Bill bill, String fieldExpr, Object value) {
        UpdateOperations<Bill> billUpdate =
                mongoStore.createUpdateOperations(Bill.class).set(fieldExpr,value);
        return mongoStore.update(bill,billUpdate);
    }

    public Key<Bill> saveBill(Bill bill) {
        return mongoStore.save(bill);
    }
}
