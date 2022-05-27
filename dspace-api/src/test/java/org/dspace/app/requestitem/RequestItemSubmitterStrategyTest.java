/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.requestitem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.dspace.AbstractUnitTest;
import org.dspace.builder.AbstractBuilder;
import org.dspace.builder.CollectionBuilder;
import org.dspace.builder.CommunityBuilder;
import org.dspace.builder.EPersonBuilder;
import org.dspace.builder.ItemBuilder;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mwood
 */
public class RequestItemSubmitterStrategyTest
        extends AbstractUnitTest {
    private static final String AUTHOR_ADDRESS = "john.doe@example.com";
    private static final String NON_AUTHOR_ADDRESS = "richard.roe@example.com";

    private static EPerson johnDoe;
    private static EPerson richardRoe;

    private Item item;

    @BeforeClass
    public static void setUpClass()
            throws SQLException {
        AbstractBuilder.init(); // AbstractUnitTest doesn't do this for us.

        Context ctx = new Context();
        ctx.turnOffAuthorisationSystem();
        johnDoe = EPersonBuilder.createEPerson(ctx)
                .withEmail(AUTHOR_ADDRESS)
                .withNameInMetadata("John", "Doe")
                .build();
        richardRoe = EPersonBuilder.createEPerson(ctx)
                .withEmail(NON_AUTHOR_ADDRESS)
                .withNameInMetadata("Richard", "Roe")
                .build();
        ctx.restoreAuthSystemState();
        ctx.complete();
    }

    @AfterClass
    public static void tearDownClass() {
        AbstractBuilder.destroy(); // AbstractUnitTest doesn't do this for us.
    }

    @Before
    public void setUp() {
        context = new Context();
        context = new Context();
        context.setCurrentUser(johnDoe);
        context.turnOffAuthorisationSystem();
        Community community = CommunityBuilder.createCommunity(context).build();
        Collection collection = CollectionBuilder.createCollection(context, community).build();
        item = ItemBuilder.createItem(context, collection)
                .build();
        context.restoreAuthSystemState();
        context.setCurrentUser(null);
    }

    /**
     * Test of getRequestItemAuthor method, of class RequestItemSubmitterStrategy.
     * @throws java.lang.Exception passed through.
     */
    @Test
    public void testGetRequestItemAuthor()
            throws Exception {
        RequestItemSubmitterStrategy instance = new RequestItemSubmitterStrategy();
        RequestItemAuthor author = instance.getRequestItemAuthor(context, item);
        assertEquals("Wrong author address", AUTHOR_ADDRESS, author.getEmail());
    }

    /**
     * Test of isAuthorized method, of class RequestItemSubmitterStrategy.
     */
    @Test
    public void testIsAuthorized() {
        RequestItemSubmitterStrategy instance = new RequestItemSubmitterStrategy();

        // Test with correct logged-in user.
        context.setCurrentUser(johnDoe);
        assertTrue("Should be authorized", instance.isAuthorized(context, item));

        // Test with incorrect logged-in user.
        context.setCurrentUser(richardRoe);
        assertFalse("Should not be authorized", instance.isAuthorized(context, item));

        // Test when not logged in.
        context.setCurrentUser(null);
        assertFalse("Should not be authorized", instance.isAuthorized(context, item));
    }
}
