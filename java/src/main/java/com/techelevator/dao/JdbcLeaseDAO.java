package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Lease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcLeaseDAO implements LeaseDAO{


    //instance variables

    @Autowired
    private LeaseDAO leaseDAO;
    private JdbcTemplate jdbcTemplate;

    //constructor
    public JdbcLeaseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //GET Methods

    //POV: the prop mgr/ownr can retrieve all leases
    @Override
    public List<Lease> getListOfLeases(){
        List<Lease> getListOfLeases = new ArrayList<>();
        String sql = "SELECT * FROM leases";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

            while (rowSet.next()){
                getListOfLeases.add(mapRowToLease(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (NullPointerException e){
            throw new DaoException("Leases not found.", e);
        }
        return getListOfLeases;
    }

    //POV: the prop mgr/owner can filter & retrieve based on the lease ID
    @Override
    public Lease getLeaseByLeaseId(int leaseId){
        Lease getLeaseByLeaseId = null;
        String sql =
                "SELECT lease_id, user_id, prop_id, start_date, end_date, rent, lease_status, term_length\n" +
                "FROM leases\n" +
                "WHERE lease_id = ?";
        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, leaseId);
            if (rowSet.next()) {
                getLeaseByLeaseId = mapRowToLease(rowSet);
            } else {
                throw new DaoException("Lease not found.");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return getLeaseByLeaseId;
    }

    //POV: the prop mgr/owner can filter & retrieve based on the owner ID
    @Override
     public Lease getLeaseByUserId(int userId){
    Lease getLeaseByUserId = null;
    String sql =
            "SELECT lease_id, user_id, prop_id, start_date, end_date, rent, lease_status, term_length\n" +
                    "FROM leases\n" +
                    "WHERE user_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (rowSet.next()) {
                getLeaseByUserId = mapRowToLease(rowSet);
            } else {
                throw new DaoException("Lease not found.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return getLeaseByUserId;
    }

    //POST Methods
    @Override
    public Lease createLease(Lease lease) {
        Lease newLease = null;
        String sql =
                "INSERT INTO leases (user_id, prop_id, start_date, end_date, rent, lease_status, term_length)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                "RETURNING lease_id";
        try {
            int newLeaseId = jdbcTemplate.queryForObject(sql, int.class,
                    lease.getUserId(),
                    lease.getPropId(),
                    lease.getStartDate(),
                    lease.getEndDate(),
                    lease.getRent(),
                    lease.getLeaseStatus(),
                    lease.gettermLength());

            newLease = getLeaseByLeaseId(newLeaseId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newLease;
    }

    //PUT Methods
    //POV: prop mgr can update the lease status
    @Override
    public Lease updateLeaseStatus(Lease lease) {           //theoretically all pieces of an object should be passed in to updadte
        Lease updatedLease = null;
                String sql =
            "UPDATE leases\n" +
            "SET lease_status = ?\n" +
            "WHERE lease_id = ?\n" +
            ";";

        try {
           int numRows = jdbcTemplate.update(sql, lease.getLeaseStatus(), lease.getLeaseId());

           if (numRows == 0){
               throw new DaoException("Zero rows affected, expected at least one");
           } else {
               updatedLease = getLeaseByLeaseId(lease.getLeaseId());
           }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedLease;
    }

    @Override
    public BigDecimal getTotal(int id) {
        BigDecimal resultEd = null;
        String getSql = "SELECT SUM(l.rent)  FROM leases as l\n" +
                "join properties AS p ON l.prop_id = p.prop_id\n" +
                "WHERE p.owner_id = ? and l.lease_status = 'active'\n" +
                "GROUP BY p.owner_id;";
        SqlRowSet getValue = jdbcTemplate.queryForRowSet(getSql, id);
        if(!getValue.wasNull()){
            if(getValue.next()){
                resultEd = getValue.getBigDecimal("sum");
            }
        }
        return resultEd;
    }




    //MapRowSet
    private Lease mapRowToLease(SqlRowSet rowSet) {
        Lease lease = new Lease();
        lease.setLeaseId(rowSet.getInt("lease_id"));
        lease.setUserId(rowSet.getInt("user_id"));
        lease.setPropId(rowSet.getInt("prop_id"));
        lease.setStartDate(rowSet.getDate("start_date").toLocalDate());
        lease.setEndDate(rowSet.getDate("end_date").toLocalDate());
        lease.setRent(rowSet.getDouble("rent"));
        lease.setLeaseStatus(rowSet.getString("lease_status"));
        lease.setTermLength(rowSet.getInt("term_length"));
        return lease;
    }

}
