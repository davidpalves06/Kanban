package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.cards.CardComment;
import com.example.Kanban.KanbanBoard.cards.KanbanCard;
import com.example.Kanban.KanbanBoard.cards.ProgressStatus;
import com.example.Kanban.KanbanBoard.dto.*;
import com.example.Kanban.UserProfile.UserProfile;
import com.example.Kanban.UserProfile.UserProfileController;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class KanbanBoardService {
    public static final String BOARD_CREATED_SUCCESSFULLY = "Board created successfully!";
    public static final String FAIL_TO_CREATE_BOARD = "Failed to create board!";
    public static final String USER_ADDED_SUCCESSFULLY = "User added successfully";
    public static final String USER_ALREADY_ADDED = "User already added";
    public static final String USER_REMOVED_SUCCESSFULLY = "User removed successfully";
    public static final String ENTITY_NOT_FOUND = "User or Board Not Found";
    public static final String CARD_ADDED_SUCCESSFULLY = "Card added successfully";
    public static final String CARD_REMOVED_SUCCESSFULLY = "Card removed successfully";
    public static final String PROGRESS_CHANGED_SUCCESSFULLY = "Progress changed successfully!";
    public static final String COMMENT_ADDED_SUCCESSFULLY = "Comment added successfully";
    public static final String COMMENT_REMOVED_SUCCESSFULLY = "Comment removed successfully!";
    private final UserProfileController userProfileController;
    private final KanbanBoardRepository kanbanBoardRepository;

    public ResponseEntity<KanbanBoardResponse> createKanbanBoard(CreateKanbanBoardDTO createKanbanBoardDTO) {
        String userID = createKanbanBoardDTO.getUserID();
        String boardName = createKanbanBoardDTO.getBoardName();

        Optional<UserProfile> userProfileOptional = getProfileResponse(userID);

        return userProfileOptional
                .map(userProfile -> createKanbanBoardAndReturnResponse(userID, boardName, userProfile))
                .orElseGet(() -> new ResponseEntity<>(new KanbanBoardResponse(FAIL_TO_CREATE_BOARD), HttpStatus.CONFLICT));
    }

    private ResponseEntity<KanbanBoardResponse> createKanbanBoardAndReturnResponse(String userID, String boardName, UserProfile userProfile) {
        KanbanBoard kanbanBoard = createKanbanBoard(userID, boardName, userProfile);
        KanbanBoard insertedKanbanBoard = kanbanBoardRepository.insert(kanbanBoard);
        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(BOARD_CREATED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(insertedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }

    private KanbanBoard createKanbanBoard(String userID, String boardName, UserProfile userProfile) {
        KanbanBoard kanbanBoard = new KanbanBoard(boardName, userID);
        kanbanBoard.addUser(userID,userProfile.getUsername());
        return kanbanBoard;
    }

    private Optional<UserProfile> getProfileResponse(String userID) {
        ResponseEntity<UserProfile> getUserProfileResponse = userProfileController.getUserProfile(userID);
        if (getUserProfileResponse.getStatusCode().is2xxSuccessful()) {
            return Optional.of(getUserProfileResponse.getBody());
        }
        else return Optional.empty();
    }

    public ResponseEntity<KanbanBoardResponse> addUserToBoard(AddUserKanbanBoardDTO addUserKanbanBoardDTO,String boardID) {
        String userID = addUserKanbanBoardDTO.getUserID();
        Optional<UserProfile> userProfileOptional = getProfileResponse(userID);
        Optional<KanbanBoard> kanbanBoardOptional = kanbanBoardRepository.findById(boardID);


        if (userProfileOptional.isPresent() && kanbanBoardOptional.isPresent()) {
            return addUserToBoardAndReturnResponse(userID, userProfileOptional.get(), kanbanBoardOptional.get());
        }
        else return new ResponseEntity<>(new KanbanBoardResponse(ENTITY_NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<KanbanBoardResponse> addUserToBoardAndReturnResponse(String userID, UserProfile userProfile, KanbanBoard kanbanBoard) {
        boolean addUserResult = kanbanBoard.addUser(userID, userProfile.getUsername());

        if (!addUserResult) return new ResponseEntity<>(new KanbanBoardResponse(USER_ALREADY_ADDED),HttpStatus.CONFLICT);

        KanbanBoard savedKanbanBoard = kanbanBoardRepository.save(kanbanBoard);

        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(USER_ADDED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(savedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }

    public ResponseEntity<KanbanBoardResponse> removeUserFromBoard(String boardID, String userID) {
        Optional<UserProfile> userProfileOptional = getProfileResponse(userID);
        Optional<KanbanBoard> kanbanBoardOptional = kanbanBoardRepository.findById(boardID);
        if (userProfileOptional.isPresent() && kanbanBoardOptional.isPresent()) {
            return removeUserFromBoardAndReturnResponse(userID, userProfileOptional.get(), kanbanBoardOptional.get());
        }
        else return new ResponseEntity<>(new KanbanBoardResponse(ENTITY_NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<KanbanBoardResponse> removeUserFromBoardAndReturnResponse(String userID, UserProfile userProfile, KanbanBoard board) {
        board.removeUser(userID);

        KanbanBoard savedKanbanBoard = kanbanBoardRepository.save(board);

        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(USER_REMOVED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(savedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }

    public ResponseEntity<KanbanBoardResponse> addCardToBoard(AddCardKanbanBoardDTO addCardKanbanBoard, String boardID) {
        String userStory = addCardKanbanBoard.getUserStory();
        Optional<KanbanBoard> kanbanBoardOptional = kanbanBoardRepository.findById(boardID);

        if (kanbanBoardOptional.isPresent()) {
            KanbanBoardResponse kanbanBoardResponse = addCardAndReturnResponse(userStory, kanbanBoardOptional.get());
            return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
        }
        else return new ResponseEntity<>(new KanbanBoardResponse(ENTITY_NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    private KanbanBoardResponse addCardAndReturnResponse(String userStory, KanbanBoard kanbanBoard) {
        KanbanCard kanbanCard = new KanbanCard(userStory, ProgressStatus.NEW);

        kanbanBoard.addCard(kanbanCard);

        KanbanBoard savedKanbanBoard = kanbanBoardRepository.save(kanbanBoard);

        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(CARD_ADDED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(savedKanbanBoard);
        return kanbanBoardResponse;
    }

    public ResponseEntity<KanbanBoardResponse> removeCardFromBoard(String boardID, String cardID) {
        Optional<KanbanBoard> kanbanBoardOptional = kanbanBoardRepository.findById(boardID);

        if (kanbanBoardOptional.isPresent()) {
            KanbanBoard kanbanBoard = kanbanBoardOptional.get();

            return removeCardAndReturnResponse(cardID, kanbanBoard);
        }
        else return new ResponseEntity<>(new KanbanBoardResponse(ENTITY_NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<KanbanBoardResponse> removeCardAndReturnResponse(String cardID, KanbanBoard kanbanBoard) {
        kanbanBoard.removeCard(cardID);

        KanbanBoard savedKanbanBoard = kanbanBoardRepository.save(kanbanBoard);

        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(CARD_REMOVED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(savedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }

    public ResponseEntity<KanbanBoardResponse> changeCardProgress(String boardID, String cardID, ProgressStatus progressStatus) {
        Optional<KanbanBoard> kanbanBoardOptional = kanbanBoardRepository.findById(boardID);
        return kanbanBoardOptional.map(kanbanBoard -> changeCardAndReturn(cardID, progressStatus, kanbanBoard))
                .orElseGet(() -> new ResponseEntity<>(new KanbanBoardResponse(ENTITY_NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<KanbanBoardResponse> changeCardAndReturn(String cardID, ProgressStatus progressStatus, KanbanBoard kanbanBoard) {
        kanbanBoard.changeCardStatus(cardID, progressStatus);

        KanbanBoard savedKanbanBoard = kanbanBoardRepository.save(kanbanBoard);
        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(PROGRESS_CHANGED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(savedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }

    public ResponseEntity<KanbanBoardResponse> addCommentToCard(String boardID, String cardID, AddCommentDTO addCommentDTO) {
        Optional<KanbanBoard> kanbanBoardOptional = kanbanBoardRepository.findById(boardID);
        return kanbanBoardOptional.map(kanbanBoard -> addCommentAndReturn(cardID
                        , new CardComment(addCommentDTO.getUsername(), addCommentDTO.getComment())
                        , kanbanBoard))
                .orElseGet(() -> new ResponseEntity<>(new KanbanBoardResponse(ENTITY_NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<KanbanBoardResponse> addCommentAndReturn(String cardID, CardComment comment, KanbanBoard kanbanBoard) {
        kanbanBoard.addCommentToCard(cardID, comment);

        KanbanBoard savedKanbanBoard = kanbanBoardRepository.save(kanbanBoard);
        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(COMMENT_ADDED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(savedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }

    public ResponseEntity<KanbanBoardResponse> removeCommentFromCard(String boardID, String cardID, int index) {
        Optional<KanbanBoard> kanbanBoardOptional = kanbanBoardRepository.findById(boardID);
        return kanbanBoardOptional.map(kanbanBoard -> removeCommentAndReturn(cardID, index, kanbanBoard))
                .orElseGet(() -> new ResponseEntity<>(new KanbanBoardResponse(ENTITY_NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<KanbanBoardResponse> removeCommentAndReturn(String cardID, int index, KanbanBoard kanbanBoard) {
        kanbanBoard.removeCommentFromCard(cardID, index);

        KanbanBoard savedKanbanBoard = kanbanBoardRepository.save(kanbanBoard);
        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(COMMENT_REMOVED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(savedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }
}
