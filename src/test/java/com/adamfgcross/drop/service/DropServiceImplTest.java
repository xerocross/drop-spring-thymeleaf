package com.adamfgcross.drop.service;

import com.adamfgcross.drop.entity.Drop;
import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.BadRequestException;
import com.adamfgcross.drop.exception.ForbiddenRequestException;
import com.adamfgcross.drop.repository.DropRepository;
import com.adamfgcross.drop.repository.UserRepository;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DropServiceImplTest {



    @Test
    public void canCreateDropService() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
    }

    @Test
    public void canUpdateDropWhenIdMatches_happy_path() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        Drop drop = new Drop();
        drop.setId(5L); // specific ID does not matter
        drop.setText("some test text");
        User user = new User(); // the authenticated user
        // If the drop belongs to the user
        // then we save the update

        // the drop will have some user that was found in the database
        Drop foundDrop = new Drop();
        foundDrop.setUser(new User());
        Long authenticatedUserId = 10L;
        foundDrop.getUser().setId(authenticatedUserId);

        // the database finds an existing drop having the same ID
        // as the input drop, indicating that this is an update
        when(dropRepository.findById(anyLong())).thenReturn(Optional.of(foundDrop));


        // the authenticated user has the same id as the found drop
        user.setId(authenticatedUserId);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
        // this is the happy path, so the update should go through
        // without exception
        try {
            dropService.updateDrop(drop, user);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
        assert(drop.getUser()).equals(user);
        verify(dropRepository, times(1)).save(drop);
    }

    // forbidden request
    @Test
    public void cantUpdateDropWhenIdNotMatches_forbidden_request() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
        Drop drop = new Drop();
        drop.setId(5L); // specific ID does not matter
        drop.setText("some test text");
        User authenticatedUser = new User(); // the authenticated user
        // If the drop does not belong to the user
        // then we do not accept the update, we
        // throw an exception

        // the drop will have some user that was found in the database
        Drop foundDrop = new Drop();
        foundDrop.setUser(new User());
        Long authenticatedUserId = 10L;
        Long foundDropUserId = 15L; // different from authenticatedUserId
        foundDrop.getUser().setId(foundDropUserId);

        // the database finds an existing drop
        when(dropRepository.findById(anyLong())).thenReturn(Optional.of(foundDrop));
        authenticatedUser.setId(authenticatedUserId);

        assertThrows(ForbiddenRequestException.class, () -> {
            try {
                dropService.updateDrop(drop, authenticatedUser);
            } catch (Exception e) {
                System.out.println(e);
                throw e;
            }
        });
        verify(dropRepository, times(0)).save(drop);
    }

    // bad request when cannot find any drop
    // with given ID
    @Test
    public void badRequestWhenNoDropFound_bad_request() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
        Drop drop = new Drop();
        drop.setId(5L); // specific ID does not matter
        User authenticatedUser = new User(); // the authenticated user
        // the database does not find any drop
        when(dropRepository.findById(anyLong())).thenReturn(Optional.empty());
        // thus there is nothing to update
        // this situation would not arise from
        // the GUI
        assertThrows(BadRequestException.class, () -> {
            try {
                dropService.updateDrop(drop, authenticatedUser);
            } catch (Exception e) {
                System.out.println(e);
                throw e;
            }
        });
        verify(dropRepository, times(0)).save(drop);
    }

    @Test
    public void canSaveWhenIdIsNull_happy_path() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
        Drop drop = new Drop();
        // id is left null
        drop.setText("some test text");
        User authenticatedUser = new User();

        try {
            dropService.updateDrop(drop, authenticatedUser);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
        assert(drop.getUser()).equals(authenticatedUser);
        verify(dropRepository, times(1)).save(drop);
    }

    // forbidden request
    @Test
    public void cantDeleteDropWhenIdNotMatches_forbidden_request() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
        Drop drop = new Drop();
        drop.setId(5L); // specific ID does not matter
        drop.setText("some test text");
        User authenticatedUser = new User(); // the authenticated user
        // If the drop does not belong to the user
        // then we do not accept the update, we
        // throw an exception

        // the drop will have some user that was found in the database
        Drop foundDrop = new Drop();
        foundDrop.setUser(new User());
        Long authenticatedUserId = 10L;
        Long foundDropUserId = 15L; // different from authenticatedUserId
        foundDrop.getUser().setId(foundDropUserId);

        // the database finds an existing drop
        when(dropRepository.findById(anyLong())).thenReturn(Optional.of(foundDrop));
        authenticatedUser.setId(authenticatedUserId);

        assertThrows(ForbiddenRequestException.class, () -> {
            try {
                dropService.deleteDrop(drop, authenticatedUser);
            } catch (Exception e) {
                System.out.println(e);
                throw e;
            }
        });
        verify(dropRepository, times(0)).delete(drop);
    }

    // bad delete request when cannot find any drop
    // with given ID
    @Test
    public void badDeleteRequestWhenNoDropFound_bad_request() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
        Drop drop = new Drop();
        drop.setId(5L); // specific ID does not matter
        User authenticatedUser = new User(); // the authenticated user
        // the database does not find any drop
        when(dropRepository.findById(anyLong())).thenReturn(Optional.empty());
        // thus there is nothing to update
        // this situation would not arise from
        // the GUI
        assertThrows(BadRequestException.class, () -> {
            try {
                dropService.deleteDrop(drop, authenticatedUser);
            } catch (Exception e) {
                System.out.println(e);
                throw e;
            }
        });
        verify(dropRepository, times(0)).delete(drop);
    }

    @Test
    public void canDeleteDropWhenIdMatches_happy_path() {
        DropRepository dropRepository = mock(DropRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        Drop drop = new Drop();
        drop.setId(5L); // specific ID does not matter
        drop.setText("some test text");
        User user = new User(); // the authenticated user
        // If the drop belongs to the user
        // then we save the update

        // the drop will have some user that was found in the database
        Drop foundDrop = new Drop();
        foundDrop.setUser(new User());
        Long authenticatedUserId = 10L;
        foundDrop.getUser().setId(authenticatedUserId);

        // the database finds an existing drop having the same ID
        // as the input drop, indicating that this is an update
        when(dropRepository.findById(anyLong())).thenReturn(Optional.of(foundDrop));


        // the authenticated user has the same id as the found drop
        user.setId(authenticatedUserId);
        DropService dropService = new DropServiceImpl(dropRepository, userRepository);
        // this is the happy path, so the update should go through
        // without exception
        try {
            dropService.deleteDrop(drop, user);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
        verify(dropRepository, times(1)).delete(drop);
    }
}
